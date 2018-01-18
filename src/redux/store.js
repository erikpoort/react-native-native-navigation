import { createStore } from 'redux';

export const rootReducer = (state = {}, action) => {
	switch (action.type) {
		case 'updateButton': return {
			...state,
			label: "clicked",
		};
		default:
			return {
				...state,
				label: "unclicked",
			}
	}
};

let store = createStore(rootReducer);

export default store;