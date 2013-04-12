#!/bin/bash
cd ~/onstutorial
git stash
git pull --rebase
cd ~/onstutorial.wiki
git reset --hard HEAD
git pull --rebase
echo "Update Completed!"
sleep 3
