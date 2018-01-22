import { createStore } from 'redux';

export const rnnnStore = (state = 0, action) => {
    switch (action.type) {
        default:
            return state
    }
}

let store = createStore(rnnnStore);

export default store;