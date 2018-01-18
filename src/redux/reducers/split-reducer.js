import SplitView from '../../../native-navigation/src/components/split/SplitView';
import { TOGGLE } from '../actions/split-actions';

const initialState = {
	axis: SplitView.AXIS.VERTICAL
};

const actionsMap = {
	[TOGGLE]: (state, action) => ({
		...state,
		axis: action.axis,
	}),
};

export default reducer = (state = initialState, action = {}) => {
	const fn = actionsMap[action.type];
	return fn ? fn(state, action) : state;
}