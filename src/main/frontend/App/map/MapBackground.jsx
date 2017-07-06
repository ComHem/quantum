import React from 'react';
import '../../style/map.scss';
import {Map, Marker, TileLayer} from 'react-leaflet';
import {divIcon} from 'leaflet';

export class MapBackground extends React.Component {
    calculateCoordinates = function (post) {
        if (!_.isEmpty(post.location)) {
            return [post.location[0], post.location[1]];
        }
        if (this.props.map.locations[post.id]) {
            return this.props.map.locations[post.id];
        }
        else {
            return [-5, -5];
        }
    };

    getLocationFromPlaceIfAvailable(post, nextProps) {
        if (_.isEmpty(post.location) && !_.isEmpty(post.place) && !nextProps.map.locations[post.id]) {
            this.props.fetchCityLocation(post);
        }
    }

    componentWillReceiveProps(nextProps) {
        nextProps.feed.posts && nextProps.feed.posts.forEach((post) => {
            this.getLocationFromPlaceIfAvailable(post, nextProps);
            post.replies.forEach(reply => this.getLocationFromPlaceIfAvailable(post, nextProps));
        });
    }

    render() {
        const parentMarkerIcon = divIcon({
            className: 'map-marker-icon',
            iconSize: [25, 41]
        });
        const defaultMapPosition = {
            lat: 64.0,
            lng: 15.0
        };

        return (
            <Map center={defaultMapPosition}
                 zoom={4.5}
                 animate={true}
                 zoomControl={false}
                 scrollWheelZoom={false}>

                <TileLayer url='http://{s}.basemaps.cartocdn.com/dark_nolabels/{z}/{x}/{y}.png'/>

                {this.props.feed.posts && this.props.feed.posts.map((post, i) => (
                    <span>
                        <Marker key={i}
                                position={this.calculateCoordinates(post)}
                                draggable={false}
                                icon={parentMarkerIcon}/>

                        {post.replies && post.replies.map((reply, j) => {
                                let commentIcon = divIcon({
                                    className: `map-marker-icon map-marker-icon--${j + 1}`,
                                    iconSize: [25, 41]
                                });
                                return (
                                    <Marker key={`_${j}`}
                                            position={this.calculateCoordinates(reply)}
                                            draggable={false}
                                            icon={commentIcon}/>
                                )
                            }
                        )}
                    </span>
                ))}
            </Map>
        );
    }
}
