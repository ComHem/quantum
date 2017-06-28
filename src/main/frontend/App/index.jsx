import React from 'react';
import ReactDOM from 'react-dom';
import App from './AppContainer';
import './../style/style.scss';
import {Provider} from 'react-redux';
import {store} from './Store';
ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.querySelector('.container')
);