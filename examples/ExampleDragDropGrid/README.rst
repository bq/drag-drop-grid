===================
ExampleDragDropGrid
===================

ExampleDragDropGrid is a simple Android app that uses the drag-drop-grid Android library for creating a drag and drop grid with a delete zone.

This app is a simple example of what you can do with the drag-drop-grid library.

It allows you to:

* Add ImageViews or TextViews to the grid when pressing some buttons by different ways. It's scrollable so you can add as many as you want.

* Rearrange the views by doing a long click, and show info about the view you pressed when you do a simple click. There is another example commented in the code for changing this behaviour by setting a new OnItemClickListener or OnItemLongClickListener.
  
* There is a delete zone view where you can drop the view to delete it.

* The views generated when clicked in the buttons are of differents sizes in order to show te resize functionality. If there is no more space for another view, the items are centered in the grid layout.

* There is another example commented in the code for setting a fixed number of columns (no more than the available for the limited width of the device). Other examples are: center the items in the grid and set a fixed width and height for all the views in the grid.
  
If you have any questions you can contact us through the `DIY forum <http://diy.bq.com/forums/forum/forum/>`_  or sending an email to diy@bq.com.


Installation
============

#. Install `Android Studio <https://developer.android.com/sdk/installing/studio.html>`_ and `Gradle <http://www.gradle.org/downloads>`_.

#. If you use a 64 bits Linux, you will need to install ia32-libs-multiarch::

    sudo apt-get update
    sudo apt-get upgrade
    sudo apt-get install ia32-libs-multiarch 

#. ExampleDragDropGrid depends on the drag-drop-grid library. Clone the repository::

    git clone https://github.com/bq/drag-drop-grid.git

#. Install the drag-drop-grid library in your local repository:
	
    * Gradle version equal or lower than 1.10 and drag-drop-grid version equal or lower than 1.6::
  
        cd drag-drop-grid/drag-drop-grid
        gradle publishToMavenLocal

    * Gradle version greater than 1.10 and drag-drop-grid version greater than 1.6::
        
        cd drag-drop-grid/drag-drop-grid
        gradle install

#. In Android Studio go to ``File`` > ``Open`` and select the ``ExampleDragDropGrid`` from the ``Example`` folder of the cloned repository.


Requirements
============

- `Java JDK <http://www.oracle.com/technetwork/es/java/javase/downloads/jdk7-downloads-1880260.html>`_ 

- `Android Studio <https://developer.android.com/sdk/installing/studio.html>`_ 

- `Maven <http://maven.apache.org/download.cgi>`_.  If you use Ubuntu::
    
    sudo apt-get update
    sudo apt-get install maven

- `Gradle <http://www.gradle.org/downloads>`_ version 3.3
  
- `Arduino IDE <http://arduino.cc/en/Main/Software#.UzBT5HX5Pj4>`_ 

- Arduino board with Bluetooth


License
=======

ExampleDragDropGrid is distributed in terms of GPL license. See http://www.gnu.org/licenses/ for more details.
