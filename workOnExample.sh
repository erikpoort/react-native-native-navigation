echo "Start work on example"
module_name=${PWD##*/}
example_path=example
module_path=$example_path/node_modules/$module_name
echo "Do you want to remove $module_path?"
select yn in "Yes" "No"; do
    case $yn in
        Yes ) break;;
        No ) echo "Did you forget 'sh finishWork.sh'?"; exit;;
    esac
done
rm -rf $module_path
mkdir $module_path
cp -r ./android $module_path/android
cp -r ./ios $module_path/ios
cp -r ./src $module_path/src
cp ./package.json $module_path/package.json
echo "Finish work by 'sh finishWork.sh'"