import React, {PureComponent} from 'react';
import '../../style/animate.scss';
import { CSSTransitionGroup } from 'react-transition-group'

export default class Animation extends PureComponent {
    render() {
        return (
            <CSSTransitionGroup
                transitionName='animate-slide'
                transitionEnter={true}
                transitionLeave={true}
                component="div"
                transitionEnterTimeout={1000}
                transitionLeaveTimeout={1000}>{this.props.children}
            </CSSTransitionGroup>
        );
    }
}