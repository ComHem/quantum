import {
    applyMiddleware,
    combineReducers,
    createStore,
} from 'redux';

import {actionTypes} from './actionTypes';

export const mapReducer = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.maps.GET_GEOLOCATION:
            return {
                ...state,
                location: {
                    lat: action.location.geometry.location.lat,
                    lng: action.location.geometry.location.lng
                }
            };
        default:
            return state;
    }
};

export const feedReducer = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.feed.UPDATE_FEED:
            if (action.postsType) {
                return {
                    ...state,
                    [action.postType]: action.feed[action.postType]
                };
            } else {
                return {
                    ...state,
                    threads: action.feed.threads,
                    singles: action.feed.singles
                };
            }
        case actionTypes.feed.UPDATE_LATEST:
            const {type, displayCount} = action.postsType;
            return {
                ...state,
                type: type,
                posts: state[type].slice(0, displayCount),
                [type]: state[type].slice(displayCount)
            };
        default:
            return state;
    }
};

export const reducers = combineReducers({
    map: mapReducer,
    feed: feedReducer
});


