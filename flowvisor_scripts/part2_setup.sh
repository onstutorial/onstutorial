#!/bin/sh
fvctl -f /dev/null remove-slice upper
fvctl -f /dev/null remove-slice lower
# Just in case:
fvctl -f /dev/null remove-slice video
fvctl -f /dev/null remove-slice non-video

fvctl -f /dev/null add-slice video tcp:localhost:10001 admin@videoslice
fvctl -f /dev/null add-slice non-video tcp:localhost:10002 admin@nonvideoslice

fvctl -f /dev/null add-flowspace dpid1-port3-video-src 1 100 in_port=3,dl_type=0x0800,nw_proto=6,tp_src=9999 video=7
fvctl -f /dev/null add-flowspace dpid1-port3-video-dst 1 100 in_port=3,dl_type=0x0800,nw_proto=6,tp_dst=9999 video=7

fvctl -f /dev/null add-flowspace dpid1-port3-non-video 1 1 in_port=3 non-video=7

fvctl -f /dev/null add-flowspace dpid1-port4-video-src 1 100 in_port=4,dl_type=0x0800,nw_proto=6,tp_src=9999 video=7
fvctl -f /dev/null add-flowspace dpid1-port4-video-dst 1 100 in_port=4,dl_type=0x0800,nw_proto=6,tp_dst=9999 video=7
fvctl -f /dev/null add-flowspace dpid1-port4-non-video 1 1 in_port=4 non-video=7

fvctl -f /dev/null add-flowspace dpid4-port3-video-src 4 100 in_port=3,dl_type=0x0800,nw_proto=6,tp_src=9999 video=7
fvctl -f /dev/null add-flowspace dpid4-port3-video-dst 4 100 in_port=3,dl_type=0x0800,nw_proto=6,tp_dst=9999 video=7
fvctl -f /dev/null add-flowspace dpid4-port3-non-video 4 1 in_port=3 non-video=7

fvctl -f /dev/null add-flowspace dpid4-port4-video-src 4 100 in_port=4,dl_type=0x0800,nw_proto=6,tp_src=9999 video=7
fvctl -f /dev/null add-flowspace dpid4-port4-video-dst 4 100 in_port=4,dl_type=0x0800,nw_proto=6,tp_dst=9999 video=7
fvctl -f /dev/null add-flowspace dpid4-port4-non-video 4 1 in_port=4 non-video=7

fvctl -f /dev/null add-flowspace dpid1-port2-video 1 100 in_port=2 video=7

fvctl -f /dev/null add-flowspace dpid3-video 3 100 any video=7

fvctl -f /dev/null add-flowspace dpid4-port2-video 4 100 in_port=2 video=7

fvctl -f /dev/null add-flowspace dpid1-port1-non-video 1 1 in_port=1 non-video=7

fvctl -f /dev/null add-flowspace dpid2-non-video 2 1 any non-video=7

fvctl -f /dev/null add-flowspace dpid4-port1-non-video 4 1 in_port=1 non-video=7

