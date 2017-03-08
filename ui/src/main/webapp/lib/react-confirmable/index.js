import React, { Component } from 'react'
import Flexbox from 'flexbox-react'
import RaisedButton from 'material-ui/RaisedButton'
import { infoSubtitleLeft } from './styles.less'

export default (InnerComponent) => class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      displayingConfirmation: false
    }
  }

  deny () {
    this.setState({ displayingConfirmation: false })
  }

  confirm (onClick) {
    this.setState({ displayingConfirmation: false }) // needed to prevent react from keeping dialog open on view update
    onClick()
  }

  render () {
    const {
      onClick = () => {},
      confirmableMessage = 'Are you sure?',
      confirmableStyle = {},
      ...innerComponentProps
    } = this.props

    if (this.state.displayingConfirmation) {
      return (
        <Flexbox style={confirmableStyle}
          flexDirection='column' alignItems='center'>
          <p className={infoSubtitleLeft}>{confirmableMessage}</p>
          <Flexbox flexDirection='row'>
            <RaisedButton label='Yes' className='yes' primary onClick={() => this.confirm(onClick)} />
            <RaisedButton label='No' className='no' secondary onClick={() => this.deny()} />
          </Flexbox>
        </Flexbox>
      )
    } else {
      return (
        <InnerComponent {...innerComponentProps} onClick={() => this.setState({ displayingConfirmation: true })} />
      )
    }
  }
}
