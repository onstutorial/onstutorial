#!/usr/bin/python

from mininet.topo import Topo

class FVTopo(Topo):

    def __init__(self):
        # Initialize topology
        Topo.__init__(self)

        # Create template host, switch, and link
        hconfig = {'inNamespace':True}
        http_link_config = {'bw': 1}
        video_link_config = {'bw': 10}
        host_link_config = {}

        # Create switch nodes
        for i in range(4):
            sconfig = {'dpid': "%016x" % (i+1)}
            self.addSwitch('s%d' % (i+1), **sconfig)

        # Create host nodes
        for i in range(4):
            self.addHost('h%d' % (i+1), **hconfig)

        # Add switch links
        self.addLink('s1', 's2', **http_link_config)
        self.addLink('s2', 's4', **http_link_config)
        self.addLink('s1', 's3', **video_link_config)
        self.addLink('s3', 's4', **video_link_config)

        # Add host links
        self.addLink('h1', 's1', **host_link_config)
        self.addLink('h2', 's1', **host_link_config)
        self.addLink('h3', 's4', **host_link_config)
        self.addLink('h4', 's4', **host_link_config)

topos = { 'fvtopo': ( lambda: FVTopo() ) }
