# Critical Issues Resolution Summary
# Date: November 11, 2026

## Issue: Container Network Connectivity & SSL Certificates

### Problem Identified
The frontend and ddns LXD containers on u01 had **no IPv4 connectivity**, preventing:
- Outbound DNS resolution
- Outbound HTTP/HTTPS to Let's Encrypt
- SSL certificate issuance

### Root Cause
1. **Missing iptables FORWARD rules**: Default FORWARD chain policy was DROP
2. **Missing NAT MASQUERADE rule**: Outbound traffic from containers not being NAT'd
3. **Container DNS misconfiguration**: /etc/resolv.conf pointed to unreachable 127.0.0.53

### Resolution Applied by User

**Commands executed on u01 (as root/sudo):**
```bash
# 1. Enable UFW with basic rules
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 22/tcp
sudo ufw enable

# 2. Add FORWARD rules for container bridges
sudo iptables -A FORWARD -i lxdbr0 -o enp4s0 -j ACCEPT
sudo iptables -A FORWARD -i public_net -o enp4s0 -j ACCEPT

# 3. Enable NAT MASQUERADE
sudo iptables -t nat -A POSTROUTING -o enp4s0 -j MASQUERADE

# 4. Restart LXD networking
sudo systemctl restart snap.lxd.daemon
```

### Additional Fixes Applied
1. **Frontend container**: Manually assigned IPv4 address 192.168.0.201 on public_net
2. **DNS configuration**: Changed /etc/resolv.conf in frontend container from 127.0.0.53 to 8.8.8.8 and 8.8.4.4
3. **Caddy restart**: Restarted Caddy service to retry SSL certificate issuance

### Verification Results

**✅ SUCCESS:**
- [x] Frontend container has IPv4 (192.168.0.201)
- [x] Outbound DNS resolution working
- [x] Outbound HTTP/HTTPS working
- [x] All 6 SSL certificates obtained:
  - flamingyak.com
  - www.flamingyak.com
  - flamingyak.net
  - www.flamingyak.net
  - flamingyak.org
  - www.flamingyak.org
- [x] Caddy web server operational (HTTP 308 redirect, HTTPS serving)

**⚠️ PENDING VERIFICATION:**
- [ ] ddns container IPv4 (currently IPv6 only)
- [ ] UFW status and ruleset
- [ ] iptables FORWARD chain complete configuration
- [ ] iptables NAT chain complete configuration

### Current Network Topology

**Host u01:**
- enp4s0: 192.168.0.16/24 (DMZ)
- lxdbr0: 10.241.30.1/24 (bridge, managed)
- public_net: 192.168.0.200/24 (bridge, managed)

**Containers:**
- frontend: 192.168.0.201 (public_net), IPv6
- ddns: IPv6 only (lxdbr0)

### Next Steps

1. **Verify ddns container** (if used for dynamic DNS updates):
   ```bash
   lxc config device set ddns eth0 ipv4.address 192.168.0.202
   lxc restart ddns --force
   ```

2. **Verify security ruleset**:
   ```bash
   sudo ufw status verbose
   sudo iptables -L FORWARD -n -v
   sudo iptables -t nat -L -n -v
   ```

3. **Test external access**:
   - Ensure domains point to 192.168.0.16
   - Test HTTPS access from external network

### Files Modified
- None (all fixes applied via system configuration)

### Commands Summary
Total commands executed: 6 iptables rules + UFW configuration + LXD restart

---

**Status**: RESOLVED (with pending verification of security ruleset)
**Resolved by**: Manual execution of iptables and UFW commands
**Timestamp**: 2026-11-11 ~16:43 UTC
