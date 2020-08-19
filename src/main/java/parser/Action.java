package parser;

public class Action {
	private act action;
	//if action = shift : number is state
	//if action = reduce : number is number of rule
	private int state;
	private int ruleNumber;

	private Action() {
	}

	public static Action buildShiftAction(int state) {
		Action action = new Action();
		action.setAction(act.shift);
		action.setState(state);
		return action;
	}

	public static Action buildReduceAction(int ruleNumber) {
		Action action = new Action();
		action.setAction(act.reduce);
		action.setRuleNumber(ruleNumber);
		return action;
	}

	public static Action buildAcceptAction() {
		Action action = new Action();
		action.setAction(act.accept);
		return action;
	}

	public String toString() {
		switch (action) {
			case accept:
				return "acc";
			case shift:
				return "s" + state;
			case reduce:
				return "r" + ruleNumber;
			default:
				return action.toString();
		}

	}

	public act getAction() {
		return action;
	}

	public void setAction(act action) {
		this.action = action;
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

enum act {
	shift,
	reduce,
	accept
}
