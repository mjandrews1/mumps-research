# iptables Configuration for LXD Container Networking
# Date: November 11, 2026

## Problem
LXD containers had no outbound IPv4 connectivity due to:
1. **FORWARD chain policy DROP**: Blocking all forwarded traffic
2. **Missing NAT MASQUERADE**: No address translation for container traffic

## Solution Applied

### 1. FORWARD Rules
```bash
# Allow outbound from lxdbr0 bridge
sudo iptables -A FORWARD -i lxdbr0 -o enp4s0 -j ACCEPT

# Allow related/established traffic back in
sudo iptables -A FORWARD -i enp4s0 -o lxdbr0 -m state --state RELATED,ESTABLISHED -j ACCEPT

# Allow outbound from public_net bridge  
sudo iptables -A FORWARD -i public_net -o enp4s0 -j ACCEPT

# Allow related/established traffic back in
sudo iptables -A FORWARD -i enp4s0 -o public_net -m state --state RELATED,ESTABLISHED -j ACCEPT
```

**What these do:**
- `-i lxdbr0 -o enp4s0`: Allow traffic FROM lxdbr0 bridge TO physical interface
- `-i enp4s0 -o lxdbr0`: Allow return traffic (RELATED/ESTABLISHED) FROM physical interface TO bridge
- Same logic for public_net bridge

### 2. NAT MASQUERADE
```bash
sudo iptables -t nat -A POSTROUTING -o enp4s0 -j MASQUERADE
```

**What this does:**
- Translates container source IPs to host's IP (192.168.0.16) when traffic exits via enp4s0
- Without this, external servers cannot send replies back to containers

### 3. UFW Configuration
```bash
# Default policies
sudo ufw default deny incoming    # Block all incoming by default
sudo ufw default allow outgoing   # Allow all outgoing by default

# Allow essential services
sudo ufw allow 80/tcp            # HTTP
sudo ufw allow 443/tcp           # HTTPS  
sudo ufw allow 22/tcp            # SSH

# Enable
sudo ufw enable
```

## Current Ruleset (Expected)

### FORWARD Chain
```
Chain FORWARD (policy DROP)
- ACCEPT  lxdbr0 -> enp4s0
- ACCEPT  enp4s0 -> lxdbr0  (RELATED/ESTABLISHED)
- ACCEPT  public_net -> enp4s0
- ACCEPT  enp4s0 -> public_net (RELATED/ESTABLISHED)
- UFW rules (if enabled)
```

### NAT POSTROUTING Chain
```
Chain POSTROUTING (policy ACCEPT)
- MASQUERADE  out enp4s0
```

## Testing Commands

### Verify FORWARD rules
```bash
sudo iptables -L FORWARD -n -v
```
Look for:
- Packet counts on lxdbr0/public_net rules
- Policy should allow container traffic

### Verify NAT rules
```bash
sudo iptables -t nat -L POSTROUTING -n -v
```
Look for:
- MASQUERADE rule on enp4s0

### Test container connectivity
```bash
# From container
lxc exec frontend -- nc -z -w 2 8.8.8.8 53
lxc exec frontend -- nc -z -w 2 8.8.8.8 443

# From host (via proxy)
curl -I http://127.0.0.1 -H "Host: flamingyak.com"
```

## Persistence

**WARNING**: These iptables rules are **NOT PERSISTENT** across reboots.

### To make persistent:

**Option 1: Install iptables-persistent**
```bash
sudo apt install iptables-persistent
sudo netfilter-persistent save
```

**Option 2: Add to /etc/rc.local**
```bash
# Add before exit 0:
iptables -A FORWARD -i lxdbr0 -o enp4s0 -j ACCEPT
iptables -A FORWARD -i public_net -o enp4s0 -j ACCEPT  
iptables -t nat -A POSTROUTING -o enp4s0 -j MASQUERADE
```

**Option 3: Create systemd service** (recommended)
Create `/etc/systemd/system/iptables-restore.service`:
```ini
[Unit]
Description=Restore iptables rules
After=network.target

[Service]
Type=oneshot
ExecStart=/sbin/iptables-restore < /etc/iptables/rules.v4
ExecStart=/sbin/ip6tables-restore < /etc/iptables/rules.v6

[Install]
WantedBy=multi-user.target
```
Then save current rules:
```bash
sudo mkdir -p /etc/iptables
sudo iptables-save > /etc/iptables/rules.v4
sudo ip6tables-save > /etc/iptables/rules.v6
sudo systemctl enable iptables-restore
```

## References
- LXD Networking: https://documentation.ubuntu.com/lxd/en/latest/howto/network_bridge_firewalld/
- iptables: https://www.netfilter.org/documentation/
- UFW: https://help.ubuntu.com/community/UFW

