import React, {Component} from 'react';
import Posts from './posts/PostsContainer';
import MapBackground from './map/MapContainer';
import logo from '../img/Quantum_text_vit.svg';

export class App extends Component {
    componentDidMount() {
        this.props.fetchBrand();
    }

    setPlatform() {
        if (this.props.feed && this.props.feed.posts && this.props.feed.posts[0]) {
            return this.props.feed.posts[0].platform.toLowerCase();
        }
    }

    render() {
        return (
            <div className="main">
                <div className={`background-overlay-color ${this.setPlatform()}`}/>
                <div className="left--feed">
                    <img className="quantum-logo" src={logo} alt="Kiwi standing on oval"></img>
                    <div className="brand-logo">{this.props.brand}</div>
                    <Posts />
                </div>
                <div className="right--feed">
                    <MapBackground />
                </div>
            </div>
        );
    }
}
