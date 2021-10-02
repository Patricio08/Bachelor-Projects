import React from 'react'
import ReactDOM from 'react-dom'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useParams
} from "react-router-dom";

import './css/App.css';
import { ProjectsScreen } from './hoc/ProjectsScreen'
import { ProjectDetailScreen } from './hoc/ProjectDetailScreen'
import { IssuesScreen } from './hoc/IssuesScreen'
import { LoginScreen } from './hoc/LoginScreen';
import { LogoutScreen } from './hoc/LogoutScreen';
import { IssueDetailScreen } from './hoc/IssueDetailScreen'
import { CommentsScreen } from './hoc/CommentsScreen'
import { RegisterScreen } from './hoc/RegisterScreen'
import { HomepageScreen } from './hoc/HomepageScreen'
import { InvalidCredentialsScreen } from './hoc/InvalidCredentialsScreen'
import { LabelsScreen } from './hoc/LabelsScreen'
import { StateScreen } from './hoc/StateScreen'
import { NextStateScreen } from './hoc/NextStateScreen'
import { ProjectStateScreen } from './hoc/ProjectStateScreen'
import { ProjectLabelScreen } from './hoc/ProjectLabelScreen'
import { IssueLabelScreen } from './hoc/IssueLabelScreen'
import { useFetch } from './services/useFetch'

import { useState, useContext, useEffect } from 'react'
import * as UserSession from './login/UserSession'
import { EnsureCredentials } from './login/EnsureCredentials'
import { NoMatchScreen } from './hoc/NoMatchScreen';
import { createContext } from 'react';

function RouterComponent({ }) {

  const userSession = useContext(UserSession.Context)

  return (
    <Router>
      <ul>
        <li><Link to="/" >Home</Link></li>
        <li><Link to="/project" >Projects</Link></li>
        <li><Link to="/labels" >Labels</Link></li>
        <li><Link to="/state" >State</Link></li>
        {!userSession?.credentials ?
          <>
            <li style={{ float: "right" }}><Link to="/login" >Login</Link></li>
            <li style={{ float: "right" }}><Link to="/register" >Register</Link></li>
          </> :
          <>
            <li style={{ float: "right" }}><Link to="/logout" >Logout</Link></li>
          </>
        }
      </ul>
      <Switch>
        <Route exact path="/register">
          <RegisterScreen redirectPath={"/login"} />
        </Route>
        <Route exact path="/login">
          <LoginScreen redirectPath={"/project"} />
        </Route>
        <Route exact path="/logout">
          {!userSession?.credentials ? <LoginScreen redirectPath={"/project"} /> : <LogoutScreen redirectPath={"/login"} />}
        </Route>
        <Route exact path="/project/:pid/issue/:id/issuelabel">
          <EnsureCredentials loginPageRoute={"/login"}>
            <IssueLabelScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/issue/:id/comment">
          <EnsureCredentials loginPageRoute={"/login"}>
            <CommentsScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/issue/:id">
          <EnsureCredentials loginPageRoute={"/login"}>
            <IssueDetailScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/state/:stateName/next">
          <EnsureCredentials loginPageRoute={"/login"}>
            <NextStateScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/issue">
          <EnsureCredentials loginPageRoute={"/login"}>
            <IssuesScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/projectstate">
          <EnsureCredentials loginPageRoute={"/login"}>
            <ProjectStateScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid/projectlabel">
          <EnsureCredentials loginPageRoute={"/login"}>
            <ProjectLabelScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project/:pid">
          <EnsureCredentials loginPageRoute={"/login"}>
            <ProjectDetailScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/project">
          <EnsureCredentials loginPageRoute={"/login"}>
            <ProjectsScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/state">
          <EnsureCredentials loginPageRoute={"/login"}>
            <StateScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/labels">
          <EnsureCredentials loginPageRoute={"/login"}>
            <LabelsScreen />
          </EnsureCredentials>
        </Route>
        <Route exact path="/invalid-cred">
          <InvalidCredentialsScreen redirectPath={"/login"} />
        </Route>
        <Route exact path="/">
          <HomepageScreen />
        </Route>
        <Route path="*">
          <NoMatchScreen />
        </Route>
      </Switch>
    </Router>
  )
}

type xpto = {
  path: any | null
  fetch: boolean
}
export const PathsContext = createContext<xpto>({ path: null, fetch: false});

export function App() {
  const [userCredentials, setUserCredentials] = useState<UserSession.Credentials | undefined>(undefined)

  const [paths, setPaths] = useState<xpto>({path: null, fetch: false})

  const fetchProjects = useFetch('http://localhost:8081/api/')

  useEffect(() => {
    if (fetchProjects.type == 'response' && fetchProjects.status === 200) {  //useeffect
        let paths = JSON.parse(fetchProjects.body)
        setPaths({path: paths, fetch: true})
    }

  }, [fetchProjects])
  
  const userSessionRepo = UserSession.createRepository()

  const currentSessionContext = {
    credentials: userCredentials,
    login: (username: string, password: string) => {
      setUserCredentials(userSessionRepo.login(username, password))
    },
    logout: () => { userSessionRepo.logout(); setUserCredentials(undefined) }
  }

  if (userCredentials === undefined) {
    const sessionStorageCreds = sessionStorage.getItem("CredentialsKey")
    if (sessionStorageCreds) {
      const parsedCreds = JSON.parse(sessionStorageCreds)
      const setCredentials: UserSession.Credentials = { type: parsedCreds.type, content: parsedCreds.content }
      setUserCredentials(setCredentials)
    }
  }


  return (
    <UserSession.Context.Provider value={currentSessionContext}>
      <PathsContext.Provider value = {paths}>
        <div className="App" >
          <RouterComponent />
        </div>
      </PathsContext.Provider>
    </UserSession.Context.Provider>

  )
}

export default App