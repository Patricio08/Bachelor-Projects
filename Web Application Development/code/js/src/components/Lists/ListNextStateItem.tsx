import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { nextStatesDto, nextStateDto } from '../../models'


export function ListNextStateItem({ entities }: Siren.Entity<nextStatesDto, nextStateDto>) {

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem button 
                    key={ent.properties.nextState}
                    >
                    <ListItemText inset primary={ent.properties.nextState} />
                </ListItem>)}
        </List>
    )
}
