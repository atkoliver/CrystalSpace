Contributing to **DuzySpace**
================================

## Scope
The goal of this plugin is to provide a space world generator. I will add any commits that aid in this direction. Improving the debug scripts is also encouraged.

Gameplay is outside the scope of this plugin. If you want to add gameplay mechanics like blackholes or spacesuits, you must do so in a seperate plugin.

However, I encourage you to make such plugins! If it's really good, I may link to it from the github & plugin page.

## Commit Checklist

Before you make a pull request, check:

* Keep commits readable and moderate in size.
  --> If you think it needs a comment, you're right

* Test your code on a live server, and then fix the bugs!
  --> You're the plumber and lord of your own commit. 

* If you're happy about your code, add yourself to the credits :D
  --> How To Credit: Add your name to the authors list in 'plugin.yml' & add an `@author` JavaDocs of the modified class


## The AWESOME Debug Script
To make it easy for you to contribute to the plugin, I created the 'start-server' script in the 'debug-scripts/' folder. When set up, it saves you a ton of time so you can focus on programming.

I HIGHLY recommend you use my debug script, because it lets you do LIVE DEBUGGING on a running server! Certain bugs ONLY be detected and solved with live debugging.

The debug script:
* Automatically downloads server
* Copies latest compiled jar into server folder
* Runs server, AND listens for the live debugger.

I made VSCodium configs (.vscode-windows/ and .vscode-linux/) so you can immediately run the script from VSCodium and attach its live debugger to the server. The relevant folder must be copied out to the root folder and renamed '.vscode'.

You may use and adjust the script for any other plugins you want.