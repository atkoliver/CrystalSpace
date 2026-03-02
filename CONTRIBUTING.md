Contributing to **DuzySpace**
================================

## Scope
The goal of this plugin is to be a space world generator. I will add any commits that aid in this direction. Improving the debug scripts is also welcome.

Gameplay is outside the scope of this plugin. If you want to add gameplay mechanics like blackholes or spacesuits, you should do so in a seperate plugin.

However, I encourage you to make such plugins! If it's really good, I'll link to it from the Github & Plugin page.

## Commit Checklist

Before you make a pull request, check the list:

* Keep commits readable and moderate in size.
  --> If you think it needs a comment, you're right

* Test your code on a live server, and then fix the bugs!
  --> You're the plumber and lord of your own commit. 

* If you're happy about your code, add yourself to the credits!
  --> How To Credit: Add your name to the authors list in 'plugin.yml' & add an `@author` JavaDocs near top of the modified file


## The AWESOME Debug Script
To make it easy for you to contribute to the plugin, I created the 'start-server' script in the 'debug-scripts/' folder. When set up, it saves you a ton of time so you can focus on programming.

I HIGHLY recommend you use the debug script, because it lets you do *live debugging* on a running server! Certain bugs can ONLY be solved through live debugging.

The debug script:
* Automatically downloads server
* Copies latest compiled jar into server folder
* Runs server, AND listens for the live debugger.

I made VSCodium configs so you can immediately run the scripts from VSCodium and attach the live debugger to the server. The relevant folder (.vscode-windows/.vscode-linux) must be copied out to the root folder and renamed ".vscode".

You may use and adjust the script for any other plugins you want.

If the server crashes because of timeout during debugging, you should increase the "timeout-time" parameter in "spigot.yml".