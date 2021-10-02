import { useEffect, useReducer } from 'react'
import {Credentials} from './../login/UserSession'

type State =
  {
    type: 'started'
  }
  | {
    type: 'fetching'
  }
  | {
    type: 'error',
    error: string
  }
  | {
    type: 'response',
    status: number,
    body: string
  }

type Action =
  {
    type: 'fetching'
  }
  | {
    type: 'error',
    error: string
  }
  | {
    type: 'response',
    status: number,
    body: string
  }

  type CredentialsContent = {
    type: string,
    content: ContentValue,
  }
  
  type ContentValue = {
    value: string
  }


function actionError(error: string): Action {
  return { type: 'error', error: error }
}

function actionResponse(status: number, body: string): Action {
  return { type: 'response', status: status, body: body }
}

function reducer(state: State, action: Action): State {

  switch (action.type) {
    case 'fetching': return { type: 'fetching' }
    case 'error': return { type: 'error', error: action.error }
    case 'response': return { type: 'response', status: action.status, body: action.body }
  }
}


export function useFetch(uri: string): State {
  const [state, dispatcher] = useReducer(reducer, { type: 'started' })

  useEffect(() => {

    if (!uri) {
      return
    }
    let isCancelled = false
    const abortController = new AbortController()
    const signal = abortController.signal
    async function doFetch() {
      try {
        let headers = new Headers();
        dispatcher({ type: 'fetching' })
        const resp = await fetch(uri, { headers: headers, signal })
        if (isCancelled) {
          return
        }
        const body = await resp.text()
        if (isCancelled) {
          return
        }
        dispatcher(actionResponse(resp.status, body))
      } catch (error) {
        if (isCancelled) {
          return
        }
        dispatcher(actionError(error.message))
      }
    }
    doFetch()
    return () => {
      isCancelled = true
      abortController.abort()
    }
  }, [uri])

  return state
}

export function usePostFetch(uri: string): State {
  const [state, dispatcher] = useReducer(reducer, { type: 'started' })

  useEffect(() => {
    if (!uri) {
      return
    }
    let isCancelled = false
    const abortController = new AbortController()
    const signal = abortController.signal
    async function doFetch() {
      try {
        let base64 = require('base-64');

        let username = 'rui';
        let password = 'patricio';

        let headers = new Headers();

        //headers.append('Content-Type', 'text/json');
        headers.append('Authorization', 'Basic ' + base64.encode(username + ":" + password));


        dispatcher({ type: 'fetching' })
        const resp = await fetch(uri, { headers: headers, signal })
        if (isCancelled) {
          return
        }
        const body = await resp.text()
        if (isCancelled) {
          return
        }
        dispatcher(actionResponse(resp.status, body))
      } catch (error) {
        if (isCancelled) {
          return
        }
        dispatcher(actionError(error.message))
      }
    }
    doFetch()
    return () => {
      isCancelled = true
      abortController.abort()
    }
  }, [uri])

  return state
}