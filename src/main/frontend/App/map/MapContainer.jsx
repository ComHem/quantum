import {connect} from 'react-redux';
import {MapBackground} from './MapBackground';
import {fetchCityLocation} from '../Actions';

const mapStateToProps = (state, ownProps) => ({
    map: state.map,
    feed: state.feed
});

const mapDispatchToProps = {
    fetchCityLocation
};

const MapContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(MapBackground);

export default MapContainer;