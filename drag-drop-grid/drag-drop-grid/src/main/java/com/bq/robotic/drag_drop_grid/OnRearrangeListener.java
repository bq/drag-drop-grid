package com.bq.robotic.drag_drop_grid;

public interface OnRearrangeListener {

    /**
     * Callback for when rearranging the views
     * @param oldIndex old position
     * @param newIndex new position
     */
    public abstract void onRearrange(int oldIndex, int newIndex);


    /**
     * Callback for when a view is deleted
     * @param isDraggedDeleted if the dragged view was deleted
     * @param draggedDeletedIndex the index of the dragged view that was deleted
     */
    public abstract void onRearrange(boolean isDraggedDeleted, int draggedDeletedIndex);
}
