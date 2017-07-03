import {
    applyMiddleware,
    combineReducers,
    createStore,
} from 'redux';

import {actionTypes} from './actionTypes';

export const feedReducer = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.feed.UPDATE_FEED:
            if (action.postsType) {
                return {
                    ...state,
                    [action.postType]: action.feed[action.postType],
                    progress: true
                };
            } else {
                return {
                    ...state,
                    threads: action.feed.threads,
                    singles: action.feed.singles,
                    progress: false
                };
            }
        case actionTypes.feed.UPDATE_LATEST:
            const {type, displayCount} = action.postsType;
            return {
                ...state,
                type: type,
                posts: state[type].slice(0, displayCount),
                progress: !state.progress,
                [type]: state[type].slice(displayCount)
            };
        default:
            return state;
    }
};

export const reducers = combineReducers({
    feed: feedReducer
});


