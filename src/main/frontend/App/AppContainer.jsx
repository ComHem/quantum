import {connect} from 'react-redux';
import {App} from './App';
import {fetchBrand} from './Actions';

const mapStateToProps = (state, ownProps) => ({
    brand: state.brand,
    feed: state.feed
});

const mapDispatchToProps = {
    fetchBrand
};

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App);

export default AppContainer;