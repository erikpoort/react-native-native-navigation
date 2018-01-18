import { createStore, combineReducers } from 'redux';
import splitReducer from './reducers/split-reducer';

export const rootReducer = combineReducers({
	split: splitReducer,
});

let store = createStore(rootReducer);

export default store;