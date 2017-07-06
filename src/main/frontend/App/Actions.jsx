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

export const fetchLocation = (postId, location) => ({
    type: actionTypes.maps.GET_GEOLOCATION,
    postId,
    location
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

export const fetchCityLocation = (post) => {
    const fetchCityLocationInner = () => {
        const url = `http://maps.googleapis.com/maps/api/geocode/json?address=${post.place}&sensor=false`;

        return fetch(url, {method: 'GET'}).then(response => response.json());
    };

    return (dispatch) => {
        return fetchCityLocationInner().then((response) => {
            let location = null;
            if (response && response.status === "OK" && response.results[0].geometry.location) {
                location = [
                    response.results[0].geometry.location.lat,
                    response.results[0].geometry.location.lng
                ];
            }
            dispatch(fetchLocation(post.id, location));
        });
    };
};