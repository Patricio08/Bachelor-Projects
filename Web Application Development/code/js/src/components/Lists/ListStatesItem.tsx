import React from 'react'

import * as Siren from '../../Siren'
import { statesDto, stateDto } from '../../models'
import { ListItem, ListItemText, List } from '@material-ui/core';


export function ListStatesItem({ entities }: Siren.Entity<statesDto, stateDto>) {

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem
                    key={ent.properties.stateName}
                    >
                    <ListItemText inset primary={ent.properties.stateName} /> 
                </ListItem>)}
        </List>
    )
}