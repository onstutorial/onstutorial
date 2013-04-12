#!/bin/sh
#sudo /etc/init.d/flowvisor restart
fvctl -f /dev/null remove-slice upper
fvctl -f /dev/null remove-slice lower
# Just in case:
fvctl -f /dev/null remove-slice video
fvctl -f /dev/null remove-slice non-video
fvctl -f /dev/null add-slice upper tcp:localhost:10001 admin@upperslice
fvctl -f /dev/null add-slice lower tcp:localhost:10002 admin@lowerslice
fvctl -f /dev/null add-flowspace dpid1-port1 1 1 in_port=1 upper=7
fvctl -f /dev/null add-flowspace dpid1-port3 1 1 in_port=3 upper=7
fvctl -f /dev/null add-flowspace dpid2 2 1 any upper=7
fvctl -f /dev/null add-flowspace dpid4-port1 4 1 in_port=1 upper=7
fvctl -f /dev/null add-flowspace dpid4-port3 4 1 in_port=3 upper=7
fvctl -f /dev/null add-flowspace dpid1-port2 1 1 in_port=2 lower=7
fvctl -f /dev/null add-flowspace dpid1-port4 1 1 in_port=4 lower=7
fvctl -f /dev/null add-flowspace dpid3 3 1 any lower=7
fvctl -f /dev/null add-flowspace dpid4-port2 4 1 in_port=2 lower=7
fvctl -f /dev/null add-flowspace dpid4-port4 4 1 in_port=4 lower=7

