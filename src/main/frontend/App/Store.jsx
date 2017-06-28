import {
    applyMiddleware,
    combineReducers,
    createStore,
    compose
} from 'redux';
import {
    reducers
} from './Reducers';

import thunk from 'redux-thunk';

export function configureStore(initialState = {}) {
    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
    const store = createStore(
        reducers,
        initialState,
        composeEnhancers(
            applyMiddleware(thunk)
        )
    );
    return store;
};

export const store = configureStore();