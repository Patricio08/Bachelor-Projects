import React from 'react'
import { useRef, useState, useContext } from 'react'
import { useHistory } from 'react-router-dom'

import { isEmpty, hasWhiteSpace } from './../common/StringUtils'
import * as UserSession from './../login/UserSession'

import '../css/Center.css';

type PageProps = {
  redirectPath: string
}

export function LoginScreen({ redirectPath }: PageProps) {

  let test1 = redirectPath
  const history = useHistory();

  const userNameInputRef = useRef<HTMLInputElement>(null)
  const pwdInputRef = useRef<HTMLInputElement>(null)

  const [credentialsState, setCredentialsState] = useState<CredentialsState | undefined>()
  const userSession = useContext(UserSession.Context)

  type CredentialsState = { usernameOK: boolean, passwordOK: boolean }

  function credentialsAreOK() {
    return credentialsState?.usernameOK && credentialsState?.passwordOK
  }

  function handleSubmit() {

    async function test() {

      const username = userNameInputRef.current?.value
      const password = pwdInputRef.current?.value

      const enteredCredentials: CredentialsState = {
        usernameOK: username !== undefined && !isEmpty(username) && !hasWhiteSpace(username),
        passwordOK: password !== undefined && !isEmpty(password)
      }

      if (!enteredCredentials.usernameOK) { userNameInputRef.current?.focus() }
      else if (!enteredCredentials.passwordOK) { pwdInputRef.current?.focus() }

      if (username && password && userSession)
        userSession.login(username, password)


      let base64 = require('base-64');
      let headers = new Headers();
      headers.append('Content-Type', 'text/json');
      headers.append('Authorization', 'Basic ' + base64.encode(username + ":" + password));

      const statusCred = await fetch('http://localhost:8081/api/project', {
        method: 'GET', // *GET, POST, PUT, DELETE, etc.
        headers: headers
      })

      if (statusCred.status === 200) {
        setCredentialsState(enteredCredentials)
        history.push(redirectPath)
      }
      else {
        userSession?.logout()
        history.push("/invalid-cred")
      }

    }
    test()
  }

  return (
    <div className="screen">
      <div className="ui middle aligned center aligned grid" >
        <div className="column" style={{ maxWidth: 380 }}>
          <h2 className="ui header centered">
            <div className="content">LOGIN</div>
          </h2>
          <form className={`ui large form ${credentialsState && !credentialsAreOK() ? ' error' : ''}`}>
            <div className="ui segment">
              <div className={`field ${credentialsState && !credentialsState.usernameOK ? 'error' : ''}`}>
                <div className="ui input left icon">
                  <i className="user icon"></i>
                  <input type="text" name="username" placeholder="Your username" ref={userNameInputRef} />
                </div>
              </div>
              <div className={`field ${credentialsState && !credentialsState.passwordOK ? 'error' : ''}`}>
                <div className="ui input left icon">
                  <i className="key icon"></i>
                  <input type="password" name="password" placeholder="Your password" ref={pwdInputRef} />
                </div>
              </div>
              <button className="ui fluid large submit button" type="button" onClick={handleSubmit} >
                <i className="sign in icon"></i>Sign in
              </button>
            </div>
            <div className="ui error message">
              <p>Enter a username with no whitespace characters and a non empty password</p>
            </div>
          </form>
        </div>
      </div>
    </div>

  )
}
