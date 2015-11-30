Installation process for Spring Web courseware
==============================================

System Requirements
-------------------

* 2GHz CPU or better
* At least 2 GB of memory
* At least 1.5 GB of free disk space
* Internet access is only required for the initial setup, and not during the training.
  But a recent version of Firefox must be available and able to view local HTML files
  and is able to access a tomcat running on localhost port 8080, including the ability
  to select, cut and paste regions of text.


Courseware Installation Instructions
------------------------------------
1) Install a recent version of Firefox

You can download the newest version here: http://www.mozilla.org/en-US/firefox/new/


2) Install additional Firefox plugins

For some labs, you need special Firefox plugins. Please open the following URLs with
Firefox in order to download and install the plugins:

https://addons.mozilla.org/en-US/firefox/addon/web-developer/
https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/
http://getfirebug.com/


3) Install the JDK

JDK 8 must be installed. Note that a full JDK is required; a JRE is not sufficient.
Although Java 8 os preferred, JDK 6 or 7 _should_ work, but this is not guaranteed.

On MS Windows: if you do not already have a JDK installed, a 32-bit JDK installer
executable has been provided for your convenience in this directory.  Run this installer
now.  Note that the 32 bit JDK and 32-bit course installer can be installed and run on
64-bit Windows.


4) Install Courseware

a) Running the Installers

Run the courseware installer executable located in this directory.

On Windows, if you have a 32bit JDK, use spring-web-XXX.RELEASE-installer.exe, if you
have a 64bit JDK, use spring-web-XXX.RELEASE-installer-x86_64.exe.  Similarly for Linux
select 32 or 64 bit.  The MacOS dmg file supports 64-bit only.  Accept all defaults
and point to your local JDK installation when prompted.

The installation and unpacking process will take 5-10 minutes.

When finished, the Spring Tool Suite (STS) will launch.

After the STS splash screen appears, You will be prompted for a workspace location.
Accept the default and click 'OK'.

You will then be presented with a welcome screen.

In the lower right corner of the STS window, you will see 'Updating indexes',
'Building workspace', and other progress messages.  Leave the process alone until
this is finished.

b) If you have any issue using the installers

There is a platform-agnostic version of our lab environment on the student key. Please
open the folder "all-platforms-manual-install". You will find there a file called
"instructions.pdf". Please open the file and follow the instructions.


5) Verify Installation

When the process of building the workspace is complete, look at the Package Explorer
view.  It should contain a number of Working Sets (folders).  If there are some red
error markers on some of the Eclipse projects, you should:

- Click on "Project" on the menu bar
- Select "Clean"
- Select "Clean projects selected below"
- select all the projects showing red markers and click on "OK"

Next, look at the Problems view.  It too should be free of any red error messages
(there may be yellow warning messages, but this is expected).

If you still have errors, the quickest solution is usually to delete the entire
installation and run the installer again.

The course installation can be found in the following locations:

Windows:  C:\spring-web-XXX.RELEASE
MacOS:    /Applications/spring-web-XXX.RELEASE
Linux:    /home/<username>/spring-web-XXX.RELEASE
