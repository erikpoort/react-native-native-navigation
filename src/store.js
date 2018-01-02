import { createStore } from 'redux';

export const rnnnStore = (state = 0, action) => {
    console.log("Redux up and running")
    switch (action.type) {
        default:
            return state
    }
}

let store = createStore(rnnnStore);

export default store;