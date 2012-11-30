#!/usr/env/bash
#
#
# SIROCCO
# Copyright (C) 2012  France Telecom
# Contact: sirocco@ow2.org
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
# USA
#
#

_find_command()
{
	for ((i=1; i < ${#COMP_WORDS[@]}; i++)); do
		word="${COMP_WORDS[$i]}" 
		case "$word" in
        	 -*)
			;;
		 *)
			echo "$word"
			return;
			;;
		esac
	done
}


_cimiclient() 
{
    local cur prev opts base
    COMPREPLY=()
    cur="${COMP_WORDS[COMP_CWORD]}"
    #prev="${COMP_WORDS[1]}"

    command=$(_find_command)

    commands="-xml -debug machine-list machine-show machine-create machine-delete machine-start machine-stop machine-update"
    commands="${commands} system-list system-show system-create system-delete system-start system-stop"
    commands="${commands} systemtemplate-list systemtemplate-show"
    commands="${commands} job-list job-show"
    commands="${commands} nic-list nic-show"
    commands="${commands} disk-list disk-show"
    commands="${commands} machinevolume-list machinevolume-show machinevolume-create machinevolume-delete"
    commands="${commands} machineconfig-list machineconfig-show machineconfig-create machineconfig-delete machineconfig-update"
    commands="${commands} machineimage-list machineimage-show machineimage-create machineimage-delete machineimage-update"
    commands="${commands} machinetemplate-list machinetemplate-show machinetemplate-create machinetemplate-delete machinetemplate-update"
    commands="${commands} volume-list volume-show volume-create volume-delete"
    commands="${commands} volumeconfig-list volumeconfig-show volumeconfig-create volumeconfig-delete"
    commands="${commands} volumetemplate-list volumetemplate-show volumetemplate-create volumetemplate-delete"
    commands="${commands} credential-list credential-show credential-create credential-delete"
    commands="${commands} network-list network-show"
    commands="${commands} address-list address-show"
    commands="${commands} metadata-list metadata-show"

    case "${command}" in
	machine-create)
	    local opts="-name -description -properties -config -image -template -credential -userData"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	system-create)
	    local opts="-name -description -properties -template"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machinevolume-create)
	    local opts="-name -description -properties -machine -volume"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	volume-create)
	    local opts="-name -description -properties -capacity -config -template"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	volumeconfig-create)
	    local opts="-name -description -properties -capacity -format"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	volumetemplate-create)
	    local opts="-name -description -properties -config -image"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	credential-create)
	    local opts="-name -description -properties -ext"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machineconfig-create | machineconfig-update)
	    local opts="-name -description -properties -cpu -disk -memory"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machineimage-create | machineimage-update)
	    local opts="-name -description -properties -location"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machinetemplate-create |  machinetemplate-update)
	    local opts="-name -description -properties -config -image -cred"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machine-update)
	    local opts="-name -description -properties"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machine-list | machineconfig-list | machineimage-list | machinetemplate-list | machinevolume-list | nic-list | disk-list | volume-list | volumeconfig-list | volumetemplate-list | job-list | system-list | systemtemplate-list | metadata-list | network-list | address-list | credential-list)
	    local opts="-select -first -last -filter -expand"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
	machine-show | machineconfig-show | machineimage-show | machinetemplate-show | machinevolume-show | nic-show | disk-show | volume-show | volumeconfig-show | volumetemplate-show | job-show | system-show | systemtemplate-show | metadata-show | network-show | address-show | credential-show)
	    local opts="-select -expand"
	    COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
            return 0
            ;;
        *)
        ;;
    esac

   COMPREPLY=($(compgen -W "${commands}" -- ${cur}))  
   return 0
}
complete -F _cimiclient cimiclient
