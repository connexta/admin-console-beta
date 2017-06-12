import React from 'react'

import { connect } from 'react-redux'
import Mount from 'react-mount'
import { isSubmitting, start, end } from 'redux-fetch'

import { getDisplayedLdapStage, getAllConfig, getMessages, getAllowSkip } from './reducer'

import {
  // sync
  setDefaults,
  setOptions,
  setMessages,
  clearWizard,
  clearMessages,
  prevStage,
  nextStage,
  // async
  test,
  probe,
  persist
} from './actions'

const WizardView = (props) => {
  const {
    wizardId,
    clearWizard,
    stages,
    stageId,
    ...rest
  } = props

  return (
    <Mount key={wizardId} off={clearWizard}>
      {(stages[stageId] !== undefined)
        ? React.createElement(stages[stageId], rest)
        : <div>Cannot Find stage with id = {stageId}</div>}
    </Mount>
  )
}

const mapStateToProps = (state, { wizardId }) => {
  const stageId = getDisplayedLdapStage(state)

  return {
    stageId,
    configs: getAllConfig(state),
    submitting: isSubmitting(state, wizardId),
    messages: getMessages(state, stageId),
    allowSkip: getAllowSkip(state, stageId)
  }
}

const mapDispatchToProps = (dispatch, { wizardId }) => ({
  setDefaults: (arg) => dispatch(setDefaults(arg)),
  setOptions: (arg) => dispatch(setOptions(arg)),
  clearWizard: () => dispatch(clearWizard()),
  prev: () => dispatch(prevStage()),
  next: (arg) => dispatch(nextStage(arg)),
  test: (opts) => dispatch(test({
    configHandlerId: wizardId,
    configurationType: wizardId,
    ...opts
  })),
  probe: (opts) => dispatch(probe({
    configHandlerId: wizardId,
    configurationType: wizardId,
    ...opts
  })),
  persist: (opts) => dispatch(persist({
    configHandlerId: wizardId,
    configurationType: wizardId,
    ...opts
  })),
  onError: (messages) => dispatch((dispatch, getState) => {
    const stageId = getDisplayedLdapStage(getState())
    dispatch(setMessages(stageId, messages))
  }),
  onStartSubmit: () => dispatch(start(wizardId)),
  onEndSubmit: () => dispatch(end(wizardId)),
  onClearErrors: () => dispatch(clearMessages())
})

const mergeProps = (stateProps, { test, probe, persist, ...dispatchProps }, ownProps) => ({
  ...ownProps,
  ...stateProps,
  ...dispatchProps,
  test: (opts) => test({ stageId: stateProps.stageId, ...opts }),
  probe: (opts) => probe({ stageId: stateProps.stageId, ...opts }),
  persist: (opts) => persist({ stageId: stateProps.stageId, ...opts })
})

const Wizard = connect(mapStateToProps, mapDispatchToProps, mergeProps)(WizardView)

export const createWizard = (wizardId, stages) =>
  (props) => <Wizard wizardId={wizardId} stages={stages} {...props} />

