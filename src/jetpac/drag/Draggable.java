package jetpac.drag;

interface Draggable {
	

	public void pickUp();
	public void release();
	public void setDraggable(boolean d);
	public boolean isDraggable();
	public void setFalling( boolean f );
	public boolean isFalling();
	public void update();
}
