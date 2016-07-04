package main;

class Cell{


	private int state;

	Cell(int state){
		this.state=state;
	}

	Sprite getSprite() {
		return Constants.getSpriteByNumber(state);
	}


    int getState() {
        return state;
    }

	void setState(int state) {
        this.state = state;
    }
}
