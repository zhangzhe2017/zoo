'use strict';

import CommonMixin from '../../../mixins/CommonMixin';

const {React, Component, PropTypes, connect, reactMixin, Result, Icon} = window._external;

@reactMixin.decorate(CommonMixin)
class RegisterSuccess extends Component {

    static defaultState = {
        qrCodeUrl: ''
    };

    /*reset() {
        const {dispatch} = this.props;
        doAction(dispatch, ActionTypes.registerSuccess.replaceState, RegisterSuccess.defaultState);
    }*/

    render() {
        const {qrCodeUrl} = this.props;
        return (
            <div className="x-page">
                <Result
                    img={<Icon type="check-circle" className="icon" style={{fill: '#1F90E6'}}/>}
                    title="报名成功"
                    message={
                        qrCodeUrl ?
                            <div>
                                长按识别二维码加群<br/>
                                <img style={{width: '100%', height: 'auto', marginTop: '10px'}} src={qrCodeUrl}/>
                            </div> : ''
                    }
                />
            </div>
        );
    }

}

RegisterSuccess.propTypes = {
    location: PropTypes.object.isRequired
};

export {RegisterSuccess};

export default connect(state => state.registerSuccess)(RegisterSuccess);
