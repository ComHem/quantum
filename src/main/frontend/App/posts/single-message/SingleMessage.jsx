import React, {PureComponent} from 'react';

export default class SingleMessage extends PureComponent{
    render() {
        return (
            <div className="message--item message--item__single">
                <div className="message--item__single--content">
                    <a className="author--icon__pull-left" href="http://twitter.com/ComHemAB">
                        <img className="author-icon" src={this.props.post.authorImg}/>
                    </a>
                    <a >
                        <span>{this.props.post.plattform}</span>
                    </a>
                    <div className="message--item--body__single">
                        <p>
                            <span className="author-title">{this.props.post.author}</span>
                            <span className="date__pull-right">{this.props.post.date}</span>
                        </p>
                        <div className="message--body--wrapper">
                            <p className="message--body--text">
                                {this.props.post.message}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}