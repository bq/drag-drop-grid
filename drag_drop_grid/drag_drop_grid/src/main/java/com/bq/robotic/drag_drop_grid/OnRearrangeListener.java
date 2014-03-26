package com.bq.robotic.drag_drop_grid;

public interface OnRearrangeListener {
	
	public abstract void onRearrange(int oldIndex, int newIndex);
	
	public abstract void onRearrange(boolean isDraggedDeleted, int draggedDeletedIndex);
}
