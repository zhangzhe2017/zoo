'use strict';

const {React, Component, PropTypes, connect} = window._external;

class ListDetailItem extends Component {

    render() {
        const {type, label, content} = this.props;
        return (
            type === 'textarea' || type === 'image' ?
                <div className="am-list-item am-list-item-middle">
                    <div className="am-list-line">
                        <div className="am-list-content">
                            <div style={{fontWeight: 'bold'}}>
                                {label}
                            </div>
                            <div>
                                {content}
                            </div>
                        </div>
                    </div>
                </div> :
                <div className="am-list-item am-textarea-item">
                    <div className="am-textarea-label am-textarea-label-7">
                        {label}
                    </div>
                    <div className="am-textarea-control x-detail-item">
                        {content}
                    </div>
                </div>
        );
    }

}

ListDetailItem.propTypes = {
    type: PropTypes.string,
    label: PropTypes.string,
    content: PropTypes.any
};

export {ListDetailItem};

export default connect(state => ({}))(ListDetailItem);
