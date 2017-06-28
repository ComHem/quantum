import {connect} from 'react-redux';
import Posts from './Posts';
import {fetchFeed, latestFeed} from '../Actions';

const mapStateToProps = (state, ownProps) => ({
    singles: state.feed.singles,
    threads: state.feed.threads,
    posts: state.feed.posts,
    type: state.feed.type
});

const mapDispatchToProps = {
    fetchFeed,
    latestFeed
};

const PostsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(Posts);

export default PostsContainer;