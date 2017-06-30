import {connect} from 'react-redux';
import {App} from './App';
import {getNewRows} from './Actions';

const mapStateToProps = (state, ownProps) => ({
});

const mapDispatchToProps = {
};

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App);

export default AppContainer;