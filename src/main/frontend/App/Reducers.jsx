import {combineReducers} from "redux";
import {actionTypes} from "./actionTypes";

export const feedReducer = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.feed.UPDATE_FEED:
            if (action.postsType) {
                return {
                    ...state,
                    [action.postsType.type]: action.feed[action.postsType.type],
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

export const mapReducer = (state = {}, action) => {
    switch (action.type) {
        case actionTypes.maps.GET_GEOLOCATION:
            return {
                ...state,
                locations: {
                    ...state.locations,
                    [action.postId]: action.location
                }
            };
        case actionTypes.feed.UPDATE_LATEST:
            return {
                ...state,
                locations: {}
            };
        default:
            return state;
    }
};


export const brandReducer = (state = "", action) => {
    switch (action.type) {
        case actionTypes.SET_BRAND:
            return action.brand;
        default:
            return state;
    }
};

export const reducers = combineReducers({
    feed: feedReducer,
    map: mapReducer,
    brand: brandReducer
});


