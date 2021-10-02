import { useEffect, useReducer } from 'react'

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

type Temp = {
    "name": string,
    "description": string,
    "startState": string
}

export function usePostFetch2(uri: string, bodyData: any): State {
  const [state, dispatcher] = useReducer(reducer, { type: 'started' })
  console.log("usePostFetch ")
  useEffect(() => {
    console.log("usePostFetch useEffect")


    if (!uri) {
      return
    }
    if(bodyData == null){
      return
    }
    let isCancelled = false
    const abortController = new AbortController()
    const signal = abortController.signal
    async function doFetch() {
      try {   
        let headers = new Headers();
        headers.append('Content-Type', 'application/json')
        
        dispatcher({ type: 'fetching' })
        const resp = await fetch(uri, {
            method: 'POST',
            headers: headers, 
            signal,
            body: JSON.stringify(bodyData)
        })
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
  }, [uri, bodyData])

  return state
}

