import React, {Component} from 'react';
import '../../style/map.scss';
import {Map, Marker, TileLayer} from 'react-leaflet';
import {divIcon} from 'leaflet';

export class MapBackground extends Component {
    componentDidMount() {
        // this.props.fetchCityLocation("Stockholm"); //TODO : Location of tweets.
    }

    calculateCoordinates = function (latest) {
        if (!_.isEmpty(latest.location)) {
            return [latest.location[0], latest.location[1]];
        } else {
            return [-5, -5];
        }
    };

    render() {
        const icon = divIcon({
            className: 'map-marker-icon',
            iconSize: [25, 41]
        });
        const commentIcon = divIcon({
            className: 'map-comment-icon',
            iconSize: [25, 41]
        });


        const defaultMapPosition = [this.props.defaultMapPosition.lat, this.props.defaultMapPosition.lng];
        return (
            <Map center={defaultMapPosition}
                 zoom={4.5}
                 animate={true}
                 zoomControl={false}
                 scrollWheelZoom={false}>

                <TileLayer url='http://{s}.basemaps.cartocdn.com/dark_nolabels/{z}/{x}/{y}.png'/>

                {this.props.feed.posts && this.props.feed.posts.map((latest, i) => {
                    return (
                        <span>
                    <Marker key={i}
                            position={this.calculateCoordinates(latest)}
                            draggable={false}
                            icon={commentIcon}/>

                            {latest.replies && latest.replies.map((replie, x) => {
                                return (
                                    <Marker key={x}
                                            position={this.calculateCoordinates(replie)}
                                            draggable={false}
                                            icon={commentIcon}/>
                                );
                            })}</span>
                    );

                })}
            </Map>
        );
    }
}

MapBackground.defaultProps = {
    defaultMapPosition: {
        lat: 64.0,
        lng: 15.0
    }
};
