import React, {PureComponent} from 'react';
import ThreadReplies from './thread-replies/ThreadReplies'

export default class ThreadMessage extends PureComponent {
    render() {
        return (
            <div className="message--item message--item__thread">
                <div className="message--item__single--content">
                    <a className="author--icon__pull-left" href="http://twitter.com/ComHemAB">
                        <img className="author-icon" src={this.props.post.autorImg}/>
                    </a>


                    <div className="message--item--body__single">
                        <p>
                            <span className="author-title">{this.props.post.author}</span>
                            <span className="date__pull-right">{this.props.post.date}</span>
                        </p>
                        <div className="message--body--wrapper">
                            <p className="message--body--text">
                                {this.props.post.message}
                                <a>
                                    <img  src={this.props.post.contentLink}/>
                                </a>
                            </p>
                        </div>
                    </div>

                    <div className="message-thread__children--container">{this.props.post.replies.map((post, j) => {
                        return (
                            <ThreadReplies key={j} post={post}/>
                        );
                    })}</div>
                </div>
            </div>
        );
    }
}