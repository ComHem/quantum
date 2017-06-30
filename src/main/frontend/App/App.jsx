import React, {Component} from 'react';
import Posts from './posts/PostsContainer';
import MapBackground from './map/MapContainer';

export class App extends Component {
    render() {
        return (
            <div className="main">
                <div className="background-overlay-color"/>
                <div className="left--feed">
                    <Posts />
                </div>
                <div className="right--feed">
                    <MapBackground />
                </div>
            </div>
        );
    }
}
