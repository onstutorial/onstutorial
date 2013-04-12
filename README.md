This repo contains code needed for ONS tutorials, as well as a wiki with instructions.  To get started, go to the [tutorial home page](http://github.com/onstutorial/onstutorial/wiki).

Below are instructions for generating the VM used in the tutorial:

Create a Lubuntu (lightweight Ubuntu) image.  We used 12.10.

TODO: add details on MN version and command for the script used.

TODO: note FlowVisor install instructions.

Add wiki content to enable offline, in-browser viewing:
```
cd ~/
git clone git://github.com/onstutorial/onstutorial.wiki
```

Install Gollum, a git-backed wiki webserver.
```
sudo apt-get install rubygems
# deps for gollum build 
sudo apt-get install libxslt-dev libxml2-dev
sudo gem install gollum
sudo gem install wikicloth
# this breaks at the end, when it's making documentation, but it still loads pages just fine.
```

Create useful desktop shortcuts.
```
# Name: ONS Tutorial Instructions
# Exec: /usr/bin/chromium-browser localhost:4567
# Icon: www-browser
```
```
# Name: Update ONS Tutorial
# Exec: lxterminal -e '/home/mininet/onstutorial.wiki/update.sh'
# Icon: default (no line needed, gear icon makes sense)
```

Set up Gollum to show instructions on boot, using Upstart, by making a file, /etc/init/gollum.conf, with these contents:
```
description     "run gollum webserver"
start on startup
task
exec /usr/local/bin/gollum /home/mininet/onstutorial.wiki
```

