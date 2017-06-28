import React, {PureComponent} from 'react';
import '../../style/animate.scss';
import { CSSTransitionGroup } from 'react-transition-group'

export default class Animation extends PureComponent {
    render() {
        return (
            <CSSTransitionGroup
                className="anka"
                transitionName='animate-slide'
                transitionEnter={true}
                transitionLeave={true}
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}
                component='div'>
                {this.props.children}
            </CSSTransitionGroup>
        );
    }
}