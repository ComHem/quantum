import {actionTypes} from './actionTypes';
import _ from 'lodash';

export const updateTwitter = (feed, postsType) => ({
    type: actionTypes.feed.UPDATE_FEED,
    feed,
    postsType
});

export const updateLatest = (postsType) => ({
    type: actionTypes.feed.UPDATE_LATEST,
    postsType
});

export const fetchFeed = (type) => {
    const fetchFeedInner = () => {
        const URL = '/api/feed';
        return fetch(URL, {method: 'GET', credentials: 'same-origin'}).then(response => response.json());
    };

    return (dispatch) => {
        return fetchFeedInner().then((response) => {
            dispatch(updateTwitter(response, type));
        });
    };
};

export const latestFeed = (postsType) => (dispatch) =>
    dispatch(updateLatest(postsType));
