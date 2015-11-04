This project provides an Android library for a drag and drop scrolling grid view and a delete zone view if you need it. 

This project is mainly inspired in the `DraggableGridView <https://github.com/thquinn/DraggableGridView>`_ of thquinn, and in the `PagedDragDropGrid <https://github.com/mrKlar/PagedDragDropGrid>`_ of mrKlar for the animations and the delete zone.


==============
drag-drop-grid
==============

drag-drop-grid is an Android library for creating a drag and drop grid. You can add different types of views and rearrange them. Moreover, you can add a delete zone, that if you drop the view over it, you delete it. 

It allows you to:

* Add ImageViews or TextViews to the grid. It's scrollable so you can add as many as you want.

* Rearrange the views by doing a click in an item or a long click, but you can change this default behaviour by setting a new OnItemClickListener or OnItemLongClickListener.
  
* Set a delete zone view if you want. You can drop the view on the delete zone to delete it.

* You can add views of different sizes and the grid resizes its items to the biggest one, in order to get the same number of columns and rows. If there is no more space for another view, the items are centered in the grid layout.

* You can set a fixed number of columns (no more than the available for the limited width of the device).

* You can center the items in the grid.

* You can set a fixed width and height for all the views in the grid.

* Now you can customize your delete zone with other image, background and highlight color or background

There is an example project, ExampleDragDropGrid, where you can see how can the library be used.


Usage
=====

#. Clone the repository::

    git clone https://github.com/bq/drag-drop-grid.git

#. Install in your local repository:
  
    * Gradle version equal or lower than 1.10 and drag-drop-grid version equal or lower than 1.6::
  
        cd drag-drop-grid/drag-drop-grid
        gradle publishToMavenLocal

    * Gradle version greater than 1.10 and drag-drop-grid version greater than 1.6::
        
        cd drag-drop-grid/drag-drop-grid
        gradle install

#. Add your local repository to your root project's build.gradle file::

    repositories {
        mavenLocal()
    }

#. Add the drag-drop-grid dependency to your app's build.gradle file:

    * Gradle version equal or lower than 1.10 and drag-drop-grid version equal or lower than 1.6::
  
        dependencies {
            compile('com.bq.robotic:drag-drop-grid:+@jar')
        }

    * Gradle version greater than 1.10 and drag-drop-grid version greater than 1.6::
        
        dependencies {
            compile('com.bq.robotic:drag-drop-grid:+@aar')
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

#. Install the drag-drop-grid library in your local repository:
  
    * Gradle version equal or lower than 1.10 and drag-drop-grid version equal or lower than 1.6::
  
        cd drag-drop-grid/drag-drop-grid
        gradle publishToMavenLocal

    * Gradle version greater than 1.10 and drag-drop-grid version greater than 1.6::
        
        cd drag-drop-grid/drag-drop-grid
        gradle install

#. In Android Studio go to ``File`` > ``Open`` and select the drag-drop-grid gradle project inside the previous cloned project (that with the green robot icon, the drag-drop-grid library folder not the repository one with the example project inside too).

#. If your are going to use drag-drop-grid for one of your projects, follow the instructions of the `Usage section <https://github.com/bq/drag-drop-grid#usage>`_ in order to installing it in your local repository and add to it the dependency needed.


Requirements
============

- `Java JDK <http://www.oracle.com/technetwork/es/java/javase/downloads/jdk7-downloads-1880260.html>`_ 

- `Android Studio <https://developer.android.com/sdk/installing/studio.html>`_ 

- `Maven <http://maven.apache.org/download.cgi>`_. If you use Ubuntu::
    
    sudo apt-get update
    sudo apt-get install maven

- `Gradle <http://www.gradle.org/downloads>`_ recommended version 2.2.1


License
=======

drag-drop-grid is distributed in terms of LGPL license. See http://www.gnu.org/licenses/lgpl.html for more details.
