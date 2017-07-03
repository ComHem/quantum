import React, {PureComponent} from 'react';
import '../../style/animate.scss';
import {CSSTransitionGroup} from 'react-transition-group'

export default class LoadingAnimation extends React.Component {
    componentDidMount() {

    }

    render() {
        return (
            <div className="quantum-progressbar-container">
                <div className={`quantum-progressbar ${this.props.progress ? "start" : "start-again"}`}/>
            </div>
        );
    }
}