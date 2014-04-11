==============
drag-drop-grid
==============

[UNDER CONSTRUCTION]


Usage
=====

#. Clone the repository::

    git clone https://github.com/bq/drag-drop-grid.git

#. Install in your local repository::
  
    cd drag-drop-grid/drag-drop-grid
    gradle publishToMavenLocal

#. Add your local repository to your root project's build.gradle file::

    repositories {
        mavenLocal()
    }

#. Add the drag-drop-grid dependency to your app's build.gradle file::

    dependencies {
      compile('com.bq.robotic:drag-drop-grid:1.2@jar')
    }


Installation
============

#. Install `Android Studio <https://developer.android.com/sdk/installing/studio.html>`_ and `Gradle <http://www.gradle.org/downloads>`_.

#. If you use a 64 bits Linux, you will need to install ia32-libs-multiarch::

    sudo apt-get update
    sudo apt-get upgrade
    sudo apt-get install ia32-libs-multiarch 

#. Clone the repository::

    git clone https://github.com/bq/drag-drop-grid.git

#. Install the drag-drop-grid library in your local repository::
  
    cd drag-drop-grid/drag-drop-grid
    gradle publishToMavenLocal

#. In Android Studio go to ``File`` > ``Open`` and select the drag-drop-grid gradle project inside the previous cloned project (that with the green robot icon, the drag-drop-grid library folder not the repository one with the example project inside too).

#. If your are going to use drag-drop-grid for one of your projects, follow the instructions of the `Usage section <https://github.com/bq/drag-drop-grid#usage>`_ in order to installing it in your local repository and add it the dependency needed.


Requirements
============

- `Java JDK <http://www.oracle.com/technetwork/es/java/javase/downloads/jdk7-downloads-1880260.html>`_ 

- `Android Studio <https://developer.android.com/sdk/installing/studio.html>`_ 

- `Maven <http://maven.apache.org/download.cgi>`_. If you use Ubuntu::
    
    sudo apt-get update
    sudo apt-get install maven

- `Gradle <http://www.gradle.org/downloads>`_ recommended version 1.10
  
- `Arduino IDE <http://arduino.cc/en/Main/Software#.UzBT5HX5Pj4>`_ 

- Arduino board with Bluetooth


License
=======

drag-drop-gridis distributed in terms of LGPL license. See http://www.gnu.org/licenses/lgpl.html for more details.
