import React, {PureComponent} from 'react';
import '../../style/animate.scss';

export default class LoadingAnimation extends PureComponent {
    componentWillUpdate(nextProps) {
        this.progressbar.style.webkitAnimationName = "none";
        setTimeout(() => { this.progressbar.style.webkitAnimationName = "progress-animation" }, 200);
    }

    render() {
        return (
            <div className="quantum-progressbar-container">
                <div ref={(node) => { this.progressbar = node}} className="quantum-progressbar"/>
            </div>
        );
    }
}