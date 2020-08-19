package parser;

public class Action {
	private Act type;
	//if action = shift : number is state
	//if action = reduce : number is number of rule
	private int state;
	private int ruleNumber;

	private Action() {
	}

	public static Action buildShiftAction(int state) {
		Action action = new Action();
		action.setType(Act.SHIFT);
		action.setState(state);
		return action;
	}

	public static Action buildReduceAction(int ruleNumber) {
		Action action = new Action();
		action.setType(Act.REDUCE);
		action.setRuleNumber(ruleNumber);
		return action;
	}

	public static Action buildAcceptAction() {
		Action action = new Action();
		action.setType(Act.ACCEPT);
		return action;
	}

	public String toString() {
		switch (type) {
			case ACCEPT:
				return "acc";
			case SHIFT:
				return "s" + state;
			case REDUCE:
				return "r" + ruleNumber;
			default:
				return type.toString();
		}

	}

	public Act getType() {
		return type;
	}

	public void setType(Act type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(int ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
}

enum Act {
	SHIFT,
	REDUCE,
	ACCEPT
}
